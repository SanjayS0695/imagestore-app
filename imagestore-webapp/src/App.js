import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { LOGOUT, UPLOAD_IMAGE } from "./redux/actions";
import BodyComponent from "./components/Body/BodyComponent";
import HeaderComponent from "./components/Header/HeaderComponent";
import NavbarComponent from "./components/Navbar/NavbarComponent";
import FooterComponent from "./components/Footer/FooterComponent";
import LoginPageComponent from "./components/LoginPage/LoginPageComponent.js";
import SignUpComponent from "./components/SignUp/SignUpComponent.js";
import AuthVerify from "./common/AuthVerify.js";
import "./App.css";

const App = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [image, setImage] = useState("");
  const [isAuthPage, setIsAuthPage] = useState(false);
  const [isUploadPage, setIsUploadPage] = useState(false);

  useEffect(() => {
    const pathname = location?.pathname;
    if (pathname === "/login" || pathname === "/signup") {
      setIsAuthPage(true);
    } else {
      setIsAuthPage(false);
    }
    if (pathname === "/upload") {
      setIsUploadPage(true);
    } else {
      setIsUploadPage(false);
    }
  }, [location]);

  const handleImage = (data) => {
    setImage(data);
  };

  const handleSubmit = () => {
    const formData = new FormData();
    formData.append("image", image);
    dispatch(UPLOAD_IMAGE(formData));
    navigate("/", { replace: true });
    alert("Image uploaded successfully");
  };

  const handleCancel = () => {
    setImage("");
    navigate("/", { replace: true });
  };

  const handleLogout = () => {
    dispatch(LOGOUT());
  };

  return (
    <div className="App">
      <HeaderComponent
        handleLogout={handleLogout}
        isAuthPage={isAuthPage}
      ></HeaderComponent>
      <Routes>
        <Route
          path="/login"
          element={<LoginPageComponent></LoginPageComponent>}
        />
      </Routes>
      <Routes>
        <Route path="/signup" element={<SignUpComponent></SignUpComponent>} />
      </Routes>
      {!isAuthPage && (
        <>
          <NavbarComponent></NavbarComponent>
          <BodyComponent
            image={image}
            handleImage={handleImage}
          ></BodyComponent>
          {isUploadPage && (
            <FooterComponent
              data={image}
              handleSubmit={handleSubmit}
              handleCancel={handleCancel}
            ></FooterComponent>
          )}
        </>
      )}
      <AuthVerify></AuthVerify>
    </div>
  );
};

export default App;
