/**
 * the array of options for the select box
 * @type {[{input: string[], data: string, name: string}, the name of  the weather to get
 * {input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},{input: string[], data: string, name: string},null]}
 */
export const  weatherProcessorOptions=  [
    {
        name: 'Hours Between',
        component: 'HoursBetween',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false

        },

    {
        name: 'Hours Above',
        component: 'HoursAbove',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false



        },
      {
         name: 'Hours Below',
        component: 'Hours Below',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false


          },
    {
            name: 'Max Value',
        component: 'Max',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false
    },
    {
        name: 'Min Value',
        component: 'Min',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false

    },
    {
            name: 'Total',
        component: 'Total',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false
        },
    {
        name: 'Chilling Hours Utah Method',
        component: 'UtahChill',
        monthly: false
    },
    {
        name: 'Chilling Hours Utah Method Custom',
        component: 'UtahChill',
         monthly: false

        },

    {
        name: 'First Date Above Value',
        component: 'FirstDateAboveValue',
        monthly: false
    },
    {
        name: 'First Date Below Value',
        component: 'FirstDateBelowValue',
        monthly: false
    },

    {
            name: 'Last Date Below Value',
        component: 'LastDateBelowValue',
        monthly: false
    },
    {

            name: 'Last Date Above Value',
            component: 'LastDateAboveValue',
            monthly: false
        },
    {

            name: 'Approximate Frost Line ',
        component: 'FrostLine',
        monthly: false
    },

]

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

export const weatherData = [
    { key: "Temperature 2m", value: "temperature_2m" },
    { key: "Relative Humidity 2m", value: "relative_humidity_2m" },
    { key: "Dew Point 2m", value: "dew_point_2m" },
    { key: "Apparent Temperature", value: "apparent_temperature" },
    { key: "Precipitation", value: "precipitation" },
    { key: "Rain", value: "rain" },
    { key: "Snowfall", value: "snowfall" },
    { key: "Snow Depth", value: "snow_depth" },
    { key: "Weather Code", value: "weather_code" },
    { key: "Pressure Msl", value: "pressure_msl" },
    { key: "Surface Pressure", value: "surface_pressure" },
    { key: "Cloud Cover", value: "cloud_cover" },
    { key: "Cloud Cover Low", value: "cloud_cover_low" },
    { key: "Cloud Cover Mid", value: "cloud_cover_mid" },
    { key: "Cloud Cover High", value: "cloud_cover_high" },
    { key: "ET0 Fao Evapotranspiration", value: "et0_fao_evapotranspiration" },
    { key: "Vapour Pressure Deficit", value: "vapour_pressure_deficit" },
    { key: "Wind Speed 10m", value: "wind_speed_10m" },
    { key: "Wind Speed 100m", value: "wind_speed1_00m" },
    { key: "Wind Direction 10m", value: "wind_direction_10m" },
    { key: "Wind Direction 100m", value: "wind_direction_100m" },
    { key: "Wind Gusts 10m", value: "wind_gusts_10m" },
    { key: "Soil Moisture 0 to 7cm", value: "soil_moisture_0_to_7cm" },
    { key: "Soil Temperature 0 to 7cm", value: "soil_temperature_0_to_7cm" },
    { key: "Soil Temperature 7 to 28cm", value: "soil_temperature_7_to_28cm" },
    { key: "Soil Moisture 7 to 28cm", value: "soil_moisture_7_to_28cm" },
    { key: "Soil Moisture 28 to 100cm", value: "soil_moisture_28_to_100cm" },
    { key: "Soil Temperature 28 to 100cm", value: "soil_temperature_28_to_100cm" },
    { key: "Soil Moisture 100 to 255cm", value: "soil_moisture_100_to_255cm" },
    { key: "Soil Temperature 100 to 255cm", value: "soil_temperature_100_to_255cm" }
];

