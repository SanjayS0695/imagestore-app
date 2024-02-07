import { configureStore } from "@reduxjs/toolkit";
import imageReducer from "./redux/reducer/imageReducer";
import authReducer from "./redux/reducer/imageReducer";
import createSagaMiddleware from "@redux-saga/core";
import rootSaga from "./redux/sagas/rootSaga";

const sagaMiddleware = createSagaMiddleware();

const store = configureStore({
  reducer: {
    imageReducer,
    authReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      thunk: false,
    }).concat(sagaMiddleware),
});

sagaMiddleware.run(rootSaga);
export default store;
