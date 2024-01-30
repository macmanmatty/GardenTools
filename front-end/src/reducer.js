const initialState = {
    // Your initial state here
    someValue: '',
};

const rootReducer = (state = initialState, action) => {
    // Your reducer logic here
    switch (action.type) {
        case 'SET_SOME_VALUE':
            return { ...state, someValue: action.payload };

        // Other cases...

        default:
            return state;
    }
};

export default rootReducer;