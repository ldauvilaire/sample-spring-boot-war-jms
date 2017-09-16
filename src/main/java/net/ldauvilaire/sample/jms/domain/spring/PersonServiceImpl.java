package net.ldauvilaire.sample.jms.domain.spring;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;
import net.ldauvilaire.sample.jms.domain.model.Person;
import net.ldauvilaire.sample.jms.domain.repository.PersonRepository;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class PersonServiceImpl implements PersonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Autowired
	private PersonRepository personRepository;

	@Override
	public void processPerson(PersonDTO person) {
		LOGGER.info("=> Person : {}", (person == null) ? "<none>" : person.toString());
		boolean found = false;

		List<Person> entities = this.personRepository.findByFirstNameAndLastName(
		        person.getFirstName(),
		        person.getLastName());
		int nbFound = (entities == null) ? 0 : entities.size();
        for (int i=0; i<nbFound; i++) {
            Person entity = entities.get(i);
            entity.setValue(entity.getValue()+1);
            entity.setFirstName(entity.getFirstName() + "/" + entity.getValue().toString());;
            entity.setLastName(entity.getLastName() + "/" + entity.getValue().toString());;
            entity.setComment("processPerson");
            found = true;
        }

        if (found) {
            LOGGER.info("=> Person : Nb Entities updated = {}", nbFound);
            this.personRepository.save(entities);
        } else {
            Person entity = new Person();
            {
                entity.setFirstName(person.getFirstName());
                entity.setLastName(person.getLastName());
                entity.setValue(1l);
                entity.setComment("processPerson");
            }
            LOGGER.info("=> Person : 1 New Entity saved ...");
            entity = this.personRepository.save(entity);
        }
        this.personRepository.flush();
	}

	@Override
	public void processFirstName(PersonDTO person) {
		LOGGER.info("=> FirstName : {}", (person == null) ? "" : person.getFirstName());

        List<Person> entities = this.personRepository.findByFirstName(
                person.getFirstName());
        int nbFound = (entities == null) ? 0 : entities.size();
        for (int i=0; i<nbFound; i++) {
            Person entity = entities.get(i);
            entity.setValue(entity.getValue()+1);
            entity.setFirstName(entity.getFirstName() + "/" + entity.getValue().toString());;
            entity.setComment("processFirstName");
        }
        LOGGER.info("=> FirstName : Nb Entities updated = {}", nbFound);
        this.personRepository.save(entities);
        this.personRepository.flush();
	}

	@Override
	public void processLastName(PersonDTO person) {
		LOGGER.info("=> LastName : {}", (person == null) ? "" : person.getLastName());

		List<Person> entities = this.personRepository.findByLastName(
                person.getLastName());
        int nbFound = (entities == null) ? 0 : entities.size();
        for (int i=0; i<nbFound; i++) {
            Person entity = entities.get(i);
            entity.setValue(entity.getValue()+1);
            entity.setLastName(entity.getLastName() + "/" + entity.getValue().toString());;
            entity.setComment("processLastName");
        }
        LOGGER.info("=> LastName : Nb Entities updated = {}", nbFound);
        this.personRepository.save(entities);
        this.personRepository.flush();
	}
}
