/**
 * the array of options for the select box
 * @type {[{input: string[], data: string, name: string}, the name of  the weather to get
 * {input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},null]}
 */
export const  weatherOptions=  [
    {
        name: 'Chilling Hours Min and Max',
        data: 'temperature_2m',
        component: 'minAndMaxChill',
        calcMethod: 'countBetweenMinAndMax'
    },
    {
        name: 'Chilling Hours Utah Method',
        data: 'temperature_2m',
        component: 'utahChill',
        calcMethod: 'countChillUtah'
    },
    {
        name: 'Rain Fall',
        data: 'rain',
        component: 'minMaxTotal',
        calcMethod: 'total'
    },
    {
        name: 'Snow Fall',
        data: 'snowfall',
        component: 'minMaxTotal',
        calcMethod: 'total'
    },
    {
        name: 'Precipitation',
        data: 'precipitation',
        component: 'minMaxTotal',
        calcMethod: 'total'
    },
    {
        name: 'Max Temperature',
        data: 'temperature_2m',
        component: 'minMaxTotal',
        calcMethod: 'max'

    },
    {
        name: 'Min Temperature',
        data: 'temperature_2m',
        component: 'minMaxTotal',
        calcMethod: 'min'

    }]

export const temperatureUnits= [{key:"Fahrenheit",  value:"Fahrenheit"},
    {key:"Celsius", value:''}
];
export const windSpeedUnits= [{key: "kmh", value:" KM/H"},{key: "MP/H",  value:"MP/H"}, {key:"M/S", value:  ""},{key: "Knots", value: "kn"}]

export const precipitationUnits=[{key:"Inch",  value:"inch"},
    {key:"Millimeter", value:''}]

export const simpleCalculations= [
    {key: "Max Value", value:"max"},
    {key: "Min Value",  value:"min"},
    {key:"Average Mean", value:  "mean"},
    {key: "Average Median", value: "median"},
    {key: "Total", value: "total"},
]

