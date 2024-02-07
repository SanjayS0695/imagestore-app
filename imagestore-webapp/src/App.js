import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { UPLOAD_IMAGE } from "./redux/actions";
import BodyComponent from "./components/Body/BodyComponent";
import HeaderComponent from "./components/Header/HeaderComponent";
import NavbarComponent from "./components/Navbar/NavbarComponent";
import FooterComponent from "./components/Footer/FooterComponent";
import "./App.css";

const App = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [image, setImage] = useState("");

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

  return (
    <div className="App">
      <HeaderComponent></HeaderComponent>
      <NavbarComponent></NavbarComponent>
      <BodyComponent image={image} handleImage={handleImage}></BodyComponent>
      <FooterComponent
        data={image}
        handleSubmit={handleSubmit}
        handleCancel={handleCancel}
      ></FooterComponent>
    </div>
  );
};

export default App;
