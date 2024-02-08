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
    alert(`Error: ${error?.response?.data?.message}`);
  }
}

function* registerUser(action) {
  try {
    const response = yield call(register, action?.payload);
    yield put(REGISTER_SUCCESS(response));
  } catch (error) {
    yield put(REGISTER_FAILED(error));
    alert(`Error: ${error?.response?.data?.message}`);
  }
}

function* logoutUser() {
  yield call(logout);
  window.alert("Successfully logged out!");
}

function* authSaga() {
  yield takeLatest(REGISTER, registerUser);
  yield takeLatest(LOGIN, loginUser);
  yield takeLatest(LOGOUT, logoutUser);
}

export default authSaga;
