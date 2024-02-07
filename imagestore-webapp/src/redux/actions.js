import { createAction } from "@reduxjs/toolkit";

export const UPLOAD_IMAGE = createAction("UPLOAD_IMAGE");
export const UPLOAD_IMAGE_SUCCESS = createAction("UPLOAD_IMAGE_SUCCESS");
export const UPLOAD_IMAGE_FAILED = createAction("UPLOAD_IMAGE_FAILED");

export const VIEW_ALL_IMAGES = createAction("VIEW_ALL_IMAGES");
export const VIEW_ALL_IMAGES_SUCCESS = createAction("VIEW_ALL_IMAGES_SUCCESS");
export const VIEW_ALL_IMAGES_FAILED = createAction("VIEW_ALL_IMAGES_FAILED");

export const GET_IMAGE_BY_ID = createAction("GET_IMAGE_BY_ID");
export const GET_IMAGE_BY_ID_SUCCESS = createAction("GET_IMAGE_BY_ID_SUCCESS");
export const GET_IMAGE_BY_ID_FAILED = createAction("GET_IMAGE_BY_ID_FAILED");

export const UPDATE_IMAGE_BY_ID = createAction("UPDATE_IMAGE_BY_ID");
export const UPDATE_IMAGE_BY_ID_SUCCESS = createAction(
  "UPDATE_IMAGE_BY_ID_SUCCESS"
);
export const UPDATE_IMAGE_BY_ID_FAILED = createAction(
  "UPDATE_IMAGE_BY_ID_FAILED"
);

export const DELETE_IMAGE_BY_ID = createAction("DELETE_IMAGE_BY_ID");
export const DELETE_IMAGE_BY_ID_FAILED = createAction(
  "DELETE_IMAGE_BY_ID_FAILED"
);
export const DELETE_IMAGE_BY_ID_SUCCESS = createAction(
  "DELETE_IMAGE_BY_ID_SUCCESS"
);

export const REGISTER = createAction("REGISTER");
export const REGISTER_FAILED = createAction("REGISTER_FAILED");
export const REGISTER_SUCCESS = createAction("REGISTER_SUCCESS");

export const LOGIN = createAction("LOGIN");
export const LOGIN_SUCCESS = createAction("LOGIN_SUCCESS");
export const LOGIN_FAILED = createAction("LOGIN_FAILED");

export const LOGOUT = createAction("LOGOUT");

export const SET_MESSAGE = createAction("SET_MESSAGE");
export const CLEAR_MESSAGE = createAction("CLEAR_MESSAGE");
