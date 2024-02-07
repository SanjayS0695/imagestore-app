import { put, call, takeEvery } from "redux-saga/effects";
import {
  DELETE_IMAGE_BY_ID,
  DELETE_IMAGE_BY_ID_FAILED,
  DELETE_IMAGE_BY_ID_SUCCESS,
  GET_IMAGE_BY_ID,
  GET_IMAGE_BY_ID_FAILED,
  GET_IMAGE_BY_ID_SUCCESS,
  UPDATE_IMAGE_BY_ID,
  UPDATE_IMAGE_BY_ID_FAILED,
  UPDATE_IMAGE_BY_ID_SUCCESS,
  UPLOAD_IMAGE,
  UPLOAD_IMAGE_FAILED,
  UPLOAD_IMAGE_SUCCESS,
  VIEW_ALL_IMAGES,
  VIEW_ALL_IMAGES_FAILED,
  VIEW_ALL_IMAGES_SUCCESS,
} from "../actions";
import { deleteApi, getApi, postApi, updateApi } from "../../utils/api";

function* uploadImage(action) {
  try {
    const response = yield call(
      postApi,
      "http://localhost:8080/images",
      action?.payload,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          Accept: "application/json",
        },
      }
    );
    yield put(UPLOAD_IMAGE_SUCCESS(response));
  } catch (error) {
    yield put(UPLOAD_IMAGE_FAILED(error));
  }
}

function* viewImages() {
  try {
    const response = yield call(getApi, "http://localhost:8080/images");
    yield put(VIEW_ALL_IMAGES_SUCCESS(response?.images));
  } catch (error) {
    yield put(VIEW_ALL_IMAGES_FAILED(error));
  }
}

function* getImage(action) {
  try {
    const response = yield call(
      getApi,
      `http://localhost:8080/images/${action?.payload}`
    );
    yield put(GET_IMAGE_BY_ID_SUCCESS(response));
  } catch (error) {
    yield put(GET_IMAGE_BY_ID_FAILED(error));
  }
}

function* updateImage(action) {
  try {
    const response = yield call(
      updateApi,
      `http://localhost:8080/images/${action?.payload?.id}`,
      action?.payload?.image,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    yield put(UPDATE_IMAGE_BY_ID_SUCCESS(response));
  } catch (error) {
    yield put(UPDATE_IMAGE_BY_ID_FAILED(error));
  }
}

function* deleteImage(action) {
  try {
    const response = yield call(
      deleteApi,
      `http://localhost:8080/images/${action?.payload}`
    );
    yield put(DELETE_IMAGE_BY_ID_SUCCESS(response?.images));
  } catch (error) {
    yield put(DELETE_IMAGE_BY_ID_FAILED(error));
  }
}

function* imageSaga() {
  yield takeEvery(UPLOAD_IMAGE, uploadImage);
  yield takeEvery(VIEW_ALL_IMAGES, viewImages);
  yield takeEvery(GET_IMAGE_BY_ID, getImage);
  yield takeEvery(UPDATE_IMAGE_BY_ID, updateImage);
  yield takeEvery(DELETE_IMAGE_BY_ID, deleteImage);
}

export default imageSaga;
