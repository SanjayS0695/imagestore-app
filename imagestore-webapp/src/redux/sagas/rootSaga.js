import "regenerator-runtime/runtime";
import "core-js/stable";
import ImageSaga from "./imageSaga";
import { all, fork } from "redux-saga/effects";

export default function* () {
  yield all([fork(ImageSaga)]);
}
