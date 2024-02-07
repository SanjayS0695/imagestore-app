import { call, put, takeLatest } from "redux-saga/effects";
import { login, logout, register } from "../../utils/api";
import {
  LOGIN,
  LOGIN_FAILED,
  LOGIN_SUCCESS,
  LOGOUT,
  REGISTER,
  REGISTER_FAILED,
  REGISTER_SUCCESS,
} from "../actions";

function* loginUser(action) {
  try {
    const response = yield call(login, action?.payload);
    yield put(LOGIN_SUCCESS(response));
  } catch (error) {
    yield put(LOGIN_FAILED(error));
  }
}

function* registerUser(action) {
  try {
    const response = yield call(register, action?.payload);
    yield put(REGISTER_SUCCESS(response));
  } catch (error) {
    yield put(REGISTER_FAILED(error));
  }
}

function* logoutUser(action) {
  yield call(logout);
}

function* authSaga() {
  yield takeLatest(REGISTER, registerUser);
  yield takeLatest(LOGIN, loginUser);
  yield takeLatest(LOGOUT, logoutUser);
}

export default authSaga;
