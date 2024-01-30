import React, { useState } from 'react';

const EditableLabel = ({ object, property}) => {
    const [isEditing, setIsEditing] = useState(false);
    const [inputValue, setInputValue] = useState(object[property]);

    const handleDoubleClick = () => {
        setIsEditing(true);
    };

    const handleChange = (event) => {
        setInputValue(event.target.value);
    };

    const handleSave = (value) => {
        if( property in object){
            object[property]=value;
        }

        setIsEditing(false);
    };
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            console.log("value: "+event.target.value);
            handleSave(event.target.value);
            setIsEditing(false);
            console.log("name: "+object.name);

        }
    };

    return (
        <div onDoubleClick={handleDoubleClick}>
            {property}:
            {isEditing ? (
                <input
                    value={inputValue}
                    type="text"
                    onChange={handleChange}
                    onKeyDown={handleKeyDown}
                    autoFocus
                />
            ) : (
                <span> {object[property]}</span>
            )}
        </div>
    );
};

export default EditableLabel;