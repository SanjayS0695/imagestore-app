import { useNavigate } from "react-router-dom";
import { TextField } from "@mui/material";
import { useState } from "react";
import "./UpdateImageComponent.css";
import Button from "../../common/Button/Button";
import ImageWrapper from "../../common/ImageWrapper/ImageWrapper";
import { useDispatch } from "react-redux";
import { UPDATE_IMAGE_BY_ID } from "../../../redux/actions";

const UpdateImageComponent = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [id, setId] = useState("");
  const [image, setImage] = useState("");
  const [displayImage, setDisplayImage] = useState("");

  const handleImageUpload = (event) => {
    setDisplayImage(URL.createObjectURL(event?.target?.files[0]));
    setImage(event?.target?.files[0]);
  };

  const handleId = (event) => {
    setId(event?.target?.value);
  };

  const handleUpdate = () => {
    const formData = new FormData();
    formData.append("image", image);
    const payload = {
      id,
      image: formData,
    };
    dispatch(UPDATE_IMAGE_BY_ID(payload));
    navigate("/", { replace: true });
    alert("Update the image successfully image.");
  };

  return (
    <div className="update-image-wrapper">
      <h2>Update Image</h2>
      <div className="search-box-wrapper">
        <TextField
          id="outlined-basic"
          variant="outlined"
          label="Type id of the image"
          onChange={handleId}
        ></TextField>
        {id && displayImage && (
          <Button label={"Update"} handleOnClickAction={handleUpdate}></Button>
        )}
      </div>
      <input type="file" onChange={handleImageUpload} />
      {/* <div className="image-wrapper">
        {image && <img alt="Not found" src={image} />}
      </div> */}
      <ImageWrapper image={displayImage} altText={"Not found."}></ImageWrapper>
    </div>
  );
};

export default UpdateImageComponent;
