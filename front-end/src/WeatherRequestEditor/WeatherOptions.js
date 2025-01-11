/**
 * the array of options for the select box
 * @type {[{input: string[], data: string, displayName: string}, the name of  the weather to get
 * {input: string[], data: string, displayName: string},{input: string[], data: string, displayName: string},{input: string[], data: string, displayName: string},{input: string[], data: string, displayName: string},null]}
 */
export const  weatherProcessorOptions=  [

    {key: "Hours Between",  value: {
        displayName: 'Hours Between',
        processorName: 'HoursBetween',
        monthly: true,
        hasMin: true,
        hasMax: true,
        hasValue: false
        }},

    {key: "Hours Above",  value: {
        displayName: 'Hours Above',
        processorName: 'HoursAbove',
        monthly: true,
        hasMin: false,
        hasMax: false,
        hasValue: true
        }},
      {key: "Hours Below",  value: {
         displayName: 'Hours Below',
        processorName: 'Hours Below',
        monthly: true,
              hasMin: false,
              hasMax: false,
              hasValue: true


          }},
    {key: "Max Value",  value: {
            displayName: 'Max Value',
        processorName: 'Max',
        monthly: true,
        hasMin: false,
        hasMax: true,
        hasValue: false
    }},
    {key: "Min Value",  value: {
        displayName: 'Min Value',
        processorName: 'Min',
        monthly: true,
        hasMin: true,
        hasMax: false,
        hasValue: false

    }},
    {key: "Total",  value: {
            displayName: 'Total',
        processorName: 'Total',
        monthly: true,
        hasMin: false,
        hasMax: false,
        hasValue: false
        }},
    {key: "Chilling Hours Utah Method",  value: {
        displayName: 'Chilling Hours Utah Method',
        processorName: 'UtahChill',
        monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: false
    }},
    {key: "Chilling Hours Utah Method Custom",  value: {
        displayName: 'Chilling Hours Utah Method Custom',
        processorName: 'UtahChill',
         monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: false

        }},

    {key: 'First Date Above Value',  value: {
        displayName: 'First Date Above Value',
        processorName: 'FirstDateAboveValue',
        monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: true
    }},
    {key: 'First Date Below Value',  value: {
        displayName: 'First Date Below Value',
        processorName: 'FirstDateBelowValue',
        monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: true
    }},

    {key: 'Last Date Below Value',  value: {
            displayName: 'Last Date Below Value',
        processorName: 'LastDateBelowValue',
        monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: true
    }},
    {key: 'Last Date Above Value',  value: {
            displayName: 'Last Date Above Value',
            processorName: 'LastDateAboveValue',
            monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: true
        }},
    {key: 'Approximate Frost Line',  value: {

            displayName: 'Approximate Frost Line ',
        processorName: 'FrostLine',
        monthly: false,
            hasMin: false,
            hasMax: false,
            hasValue: false
    }},

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

export const fileTypes= [
    {key: "JSON", value:"JSON"},
    {key: "CSV",  value:"CSV"},
    {key:"XLS", value:  "XLS"},
    {key: "TXT", value: "TXT"},
]

export const weatherDataTypes = [
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

