import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { LOGOUT } from "../redux/actions";

const AuthVerify = () => {
  const location = useLocation();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const path = location?.pathname;
    const user = localStorage.getItem("user");

    if (path !== "/login" && path !== "/signup") {
      const loggedInUserData = JSON.parse(user);
      if (!!loggedInUserData) {
        if (loggedInUserData?.expirationDate < Date.now()) {
          dispatch(LOGOUT());
          navigate("/login");
        }
      } else {
        dispatch(LOGOUT());
      }
    }
  }, [location]);

  return <></>;
};

export default AuthVerify;
