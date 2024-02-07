import { createReducer } from "@reduxjs/toolkit";
import {
  DELETE_IMAGE_BY_ID_FAILED,
  DELETE_IMAGE_BY_ID_SUCCESS,
  GET_IMAGE_BY_ID_FAILED,
  GET_IMAGE_BY_ID_SUCCESS,
  UPDATE_IMAGE_BY_ID_FAILED,
  UPDATE_IMAGE_BY_ID_SUCCESS,
  UPLOAD_IMAGE_FAILED,
  UPLOAD_IMAGE_SUCCESS,
  VIEW_ALL_IMAGES_FAILED,
  VIEW_ALL_IMAGES_SUCCESS,
} from "../actions";

const INITIAL_STATE = {
  images: [],
  image: null,
  uploadSuccess: false,
  error: null,
};

const imageReducer = createReducer(INITIAL_STATE, (builder) => {
  builder
    .addCase(UPLOAD_IMAGE_SUCCESS, (state, action) => {
      Object.assign(state, state);
    })
    .addCase(UPLOAD_IMAGE_FAILED, (state, action) => {
      return {
        ...state,
        error: action.error,
      };
    })
    .addCase(VIEW_ALL_IMAGES_SUCCESS, (state, action) => {
      return { ...state, images: action?.payload };
    })
    .addCase(VIEW_ALL_IMAGES_FAILED, (state, action) => {
      return {
        ...state,
        error: action?.error,
      };
    })
    .addCase(GET_IMAGE_BY_ID_SUCCESS, (state, action) => {
      return {
        ...state,
        image: action?.payload,
      };
    })
    .addCase(GET_IMAGE_BY_ID_FAILED, (state, action) => {
      return {
        ...state,
        error: action?.error,
      };
    })
    .addCase(UPDATE_IMAGE_BY_ID_SUCCESS, (state, action) => {
      return {
        ...state,
      };
    })
    .addCase(UPDATE_IMAGE_BY_ID_FAILED, (state, action) => {
      return {
        ...state,
        error: action?.error,
      };
    })
    .addCase(DELETE_IMAGE_BY_ID_SUCCESS, (state, action) => {
      return {
        ...state,
      };
    })
    .addCase(DELETE_IMAGE_BY_ID_FAILED, (state, action) => {
      return {
        ...state,
        error: action?.error,
      };
    })
    .addDefaultCase(() => {});
});

export default imageReducer;
