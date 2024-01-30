/**
 * the array of options for the select box
 * @type {[{input: string[], data: string, name: string}, the name of  the weather to get
 * {input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},null]}
 */
export const  weatherOptions=  [
    {
        name: 'Chilling Hours Min and Max',
        data: 'temperature_2m',
        input: ['minAndMax', 'startAndEndDates'],
        calcMethod: 'countBetweenMinAndMax'
    },
    {
        name: 'Chilling Hours Utah Method',
        data: 'temperature_2m',
        input: ['minAndMax', 'startAndEndDates'],
        calcMethod: 'countChillUtah'
    },
    {
        name: 'Rain Fall',
        data: 'rain',
        input: ['startAndEndDates'],
        calcMethod: 'total'
    },
    {
        name: 'Snow Fall',
        data: 'snowfall',
        input: ['startAndEndDates'],
        calcMethod: 'total'
    },
    {
        name: 'Precipitation',
        data: 'precipitation',
        input: ['startAndEndDates'],
        calcMethod: 'total'
    },
    {
        name: 'Max Temperature',
        data: 'temperature_2m',
        input: ['startAndEndDates'],
        calcMethod: 'max'

    },
    {
        name: 'Min Temperature',
        data: 'temperature_2m',
        input: ['startAndEndDates'],
        calcMethod: 'min'

    }]

export const temperatureUnits= [{key:"Fahrenheit",  value:"Fahrenheit"},
    {key:"Celsius", value:''}
];
export const windSpeedUnits= [{key: "kmh", value:" KM/H"},{key: "MP/H",  value:"MP/H"}, {key:"M/S", value:  ""},{key: "Knots", value: "kn"}]

export const precipitationUnits=[{key:"Inch",  value:"inch"},
    {key:"Millimeter", value:''}]

