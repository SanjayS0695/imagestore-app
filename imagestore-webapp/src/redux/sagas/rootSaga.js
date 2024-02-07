import "regenerator-runtime/runtime";
import "core-js/stable";
import imageSaga from "./imageSaga";
import authSaga from "./authSaga";
import { all, fork } from "redux-saga/effects";

export default function* () {
  yield all([fork(imageSaga), fork(authSaga)]);
}
