/*
    Grammar for Type B Telegram
*/
grammar Telegram;

telegram :
    addressSection /* originSection textSection */
    ;

addressSection :
    diversionLine? shortAddressLine*? normalAddressLine
    ;

diversionLine :
    START_OF_ADDRESS DIVERSION_INDICATOR SPACE RoutingIndicator END_OF_ADDRESS SPACING
    ;

shortAddressLine :
    normalAddressLine SPACING
    ;

normalAddressLine :
    START_OF_ADDRESS (PriorityCode SPACE)? (AddresseeIndicator ( SPACE | CR? LF )?)+ END_OF_ADDRESS
    ;

originSection :
    OriginatorIndicator SPACE /* DoubleSignature? MessageIdentity? */
    ;

textSection :
    START_OF_TEXT TextLine* END_OF_TEXT
    ;

RoutingIndicator :
      [A-Z0-9][A-Z0-9][A-Z0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    | [0-9][0-9][0-9][0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    ;

PriorityCode :
    [A-Z0-9][A-Z0-9]
    ;

DoubleSignature :
    [A-Z0-9][A-Z0-9] SLASH
    ;

AddresseeIndicator :
    [A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]
    ;

OriginatorIndicator :
    [A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]
    ;

TextLine :
    [^\n]+
    ;

/*
MessageIdentity :
    ([A-Z0-9] | SPACE | FULL_STOP)*
    ;
*/

/*

OriginatorIndicator :
    [A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]
    ;

DoubleSignature :
    [A-Z0-9][A-Z0-9]
    ;

TextLine :
    [^\n]+
    ;
*/

/*
telegram :
    addressSection originSection
    ;

addressSection :
    diversionLine? shortAddressLine* normalAddressLine
    ;

diversionLine :
    START_OF_ADDRESS DIVERSION_INDICATOR SPACE RoutingIndicator END_OF_ADDRESS SPACING
    ;

shortAddressLine :
    normalAddressLine SPACING
    ;

normalAddressLine :
    START_OF_ADDRESS (TwoAlpha SPACE)? (SevenAlpha ( SPACE | CR? LF )?)+ END_OF_ADDRESS
    ;

SevenAlpha :
    CityAirPortCode DepartmentCode NetworkUserDesignator
    ;

originSection :
    originatorIndicator SPACE (DoubleSignature SLASH)? (MessageIdentity LF)?
    ;

originatorIndicator :
    CityAirPortCode DepartmentCode NetworkUserDesignator
    ;

textSection :
    START_OF_TEXT Text END_OF_TEXT
    ;


RoutingIndicator :
      [A-Z0-9][A-Z0-9][A-Z0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    | [0-9][0-9][0-9][0-9] 'X' [A-Z0-9][A-Z0-9][A-Z0-9]
    ;

TwoAlpha :
    [A-Z0-9][A-Z0-9]
    ;

DoubleSignature :
    [A-Z0-9][A-Z0-9]
    ;

CityAirPortCode : [A-Z0-9][A-Z0-9][A-Z0-9] ;
DepartmentCode : [A-Z0-9][A-Z0-9] ;
NetworkUserDesignator : [A-Z0-9][A-Z0-9] ;

fragment
MessageIdentity :
    [^\n]*
    ;

fragment
Text :
    ()+
    ;
*/

SOH       : [\u0001] ;
STX       : [\u0002] ;
ETX       : [\u0003] ;
LF        : [\u000A] ;
CR        : [\u000D] ;
US        : [\u001F] ;
SPACE     : ' ' ;
FULL_STOP : '.' ;
SLASH     : '/' ;

DIVERSION_INDICATOR :
    'QSP'
    ;

SPACING :
    US  | SPACE SPACE SPACE SPACE SPACE
    ;

START_OF_ADDRESS :
    CR? LF SOH?
    ;

END_OF_ADDRESS :
    CR? LF FULL_STOP
    ;

START_OF_TEXT :
    CR? LF STX?
    ;

END_OF_TEXT :
    CR? LF ETX?
    ;
