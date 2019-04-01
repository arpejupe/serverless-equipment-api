export default function({dispatch}) {
    return next => action => {
        console.log("action", action)
        if(!action.payload || !action.payload.then) {
            return next(action);
        }

        action.payload
            .then(function(response) {
                const newAction = { ...action, payload: response.data };
                dispatch(newAction);
            });
    }
}