import React from 'react';

const ItemList = ({title, items, handleDeleteItem, handleAddItem, handleEditItem }) => {

    // Handle input changes for adding a new item (could be a form or something else)
    return (
        <div className="container mt-5">
            <h3>{title}</h3>

            {/* Button to add a new item */}
            <button className="btn btn-primary mb-3" onClick={handleAddItem}>
                Add New Item
            </button>

            <ul className="list-group">
                {items.map((item, index) => (
                    <li key={index} className="list-group-item d-flex justify-content-between ">
                        <span>{item.name} </span>
                        <button
                            className="btn btn-danger"
                            onClick={() => handleDeleteItem(index)}
                        >
                            Delete
                        </button>
                        <button
                            className="btn btn-danger"
                            onClick={() => handleEditItem(item)}
                        >
                            Edit
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ItemList;
