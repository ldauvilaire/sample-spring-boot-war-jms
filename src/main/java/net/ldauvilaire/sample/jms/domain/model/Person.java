package net.ldauvilaire.sample.jms.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity(name="person")
@SequenceGenerator(name="person_id_sequence", sequenceName="person_id_seq")
public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_id_sequence")
	private Long id;

	@Version
    @Column(name = "version")
    private int version;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "value")
    private Long value;

	@Column(name = "comment")
    private String comment;

	public Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public Long getValue() {
        return value;
    }
    public void setValue(Long value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
