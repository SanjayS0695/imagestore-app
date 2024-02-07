import { Route, Routes } from "react-router-dom";
import HomeComponent from "./Home/HomeComponent";
import UploadComponent from "./Upload/UploadComponent";
import GetImageComponent from "./GetImage/GetImageComponent";
import DeleteImageComponent from "./DeleteImage/DeleteImageComponent";
import UpdateImageComponent from "./UpdateImage/UpdateImageComponent";
import ViewComponent from "./View/ViewComponent";
import "./BodyComponent.css";

const BodyComponent = ({ handleImage, image }) => {
  return (
    <div className="body-wrapper">
      <Routes>
        <Route path="/" element={<HomeComponent></HomeComponent>} />
        <Route
          path="/upload"
          element={
            <UploadComponent
              handleImage={handleImage}
              image={image}
            ></UploadComponent>
          }
        />
        <Route
          path="/search"
          element={<GetImageComponent></GetImageComponent>}
        />
        <Route
          path="/delete"
          element={<DeleteImageComponent></DeleteImageComponent>}
        />
        <Route
          path="/update"
          element={<UpdateImageComponent></UpdateImageComponent>}
        />
        <Route path="/view" element={<ViewComponent></ViewComponent>} />
      </Routes>
    </div>
  );
};

export default BodyComponent;
