import {
  CLEAR_MESSAGE,
  LOGIN_FAIL,
  LOGIN_SUCCESS,
  LOGOUT,
  SET_MESSAGE,
} from "../actions";

const user = JSON.parse(localStorage.getItem("user"));

const INITIAL_STATE = {
  message: "",
  isLoggedIn: user ? true : false,
  user,
};

const authReducer = createReducer(INITIAL_STATE, (builder) => {
  builder
    .addCase(SET_MESSAGE, (state, action) => {
      return {
        ...state,
        imessage: action?.payload,
      };
    })
    .addCase(CLEAR_MESSAGE, (state, action) => {
      return {
        ...state,
        message: "",
      };
    })
    .addCase(REGISTER_SUCCESS, (state, action) => {
      return {
        ...state,
        isLoggedIn: false,
      };
    })
    .addCase(REGISTER_SUCCESS, (state, action) => {
      return {
        ...state,
        isLoggedIn: false,
      };
    })
    .addCase(LOGIN_SUCCESS, (state, action) => {
      return {
        ...state,
        isLoggedIn: true,
        user: action?.payload?.user,
      };
    })
    .addCase(LOGIN_FAIL, (state, action) => {
      return {
        ...state,
        isLoggedIn: false,
        user: null,
      };
    })
    .addCase(LOGOUT, (state, action) => {
      return {
        ...state,
        isLoggedIn: false,
        user: null,
      };
    })
    .addDefaultCase(() => {});
});

export default authReducer;
