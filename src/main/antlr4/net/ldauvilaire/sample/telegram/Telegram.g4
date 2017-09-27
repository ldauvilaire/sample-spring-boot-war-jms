/*
    Grammar for Type B Telegram
*/
grammar Telegram;

/* Rule(s) */
telegram :
    addressSection originSection textSection
    ;

addressSection :
    diversionLine? shortAdressLine* normalAdressLine
    ;

diversionLine :
    StartOfAddressSignal DiversionIndicator RoutingIndicator EndOfAddressSignal SpacingSignal
    ;

shortAdressLine :
    normalAdressLine SpacingSignal
    ;

normalAdressLine :
    StartOfAddressSignal? PriorityCode? addresseeIndicator+ EndOfAddressSignal
    ;

addresseeIndicator :
    CityAirPortCode DepartmentCode NetworkUserDesignator ( CR | LF | SPACE )?
    ;

originSection :
    originatorIndicator DoubleSignature? MessageIdentity?
    ;

originatorIndicator :
    CityAirPortCode DepartmentCode NetworkUserDesignator SPACE
    ;

textSection :
    StartOfTextSignal Text EndOfTextSignal
    ;


StartOfAddressSignal :
    CR | LF
    ;

DiversionIndicator :
    'QSP '
    ;

RoutingIndicator :
      [A-Z0-9][A-Z0-9][A-Z0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    | [0-9][0-9][0-9][0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    ;

EndOfAddressSignal :
    CR | LF | FULL_STOP
    ;

SpacingSignal :
    '     '
    ;

PriorityCode :
    ([A-Z0-9][A-Z0-9]) SPACE
    ;

DoubleSignature :
    ([A-Z0-9][A-Z0-9]) SLASH
    ;

MessageIdentity :
    ([A-Z0-9]*) LF
    ;

CityAirPortCode : ([A-Z0-9][A-Z0-9][A-Z0-9]) ;
DepartmentCode : ([A-Z0-9][A-Z0-9]) ;
NetworkUserDesignator : ([A-Z0-9][A-Z0-9]) ;

Text :
    ()+
    ;

StartOfTextSignal :
    CR | LF
    ;

EndOfTextSignal :
    CR | LF
    ;


CR : '\r' ;
LF : '\n' ;
SPACE : ' ' ;
FULL_STOP : '.' ;
SLASH : '/' ;
