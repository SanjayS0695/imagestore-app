import { TextField } from "@mui/material";
import { useState } from "react";
import "./DeleteImageComponent.css";
import Button from "../../common/Button/Button";
import { useDispatch } from "react-redux";
import { DELETE_IMAGE_BY_ID } from "../../../redux/actions";

const DeleteImageComponent = () => {
  const dispatch = useDispatch();
  const [id, setId] = useState("");

  const handleSearchText = (event) => {
    setId(event?.target?.value);
  };

  const handleDelete = () => {
    if (id) {
      dispatch(DELETE_IMAGE_BY_ID(id));
      alert("Succefully deleted image with id: " + id);
    }
  };
  return (
    <div className="delete-image-wrapper">
      <h2>Delete Image by Id</h2>
      <div className="delete-search-box-wrapper">
        <TextField
          id="outlined-basic"
          variant="outlined"
          label="Type id of the image to be deleted"
          onChange={handleSearchText}
        ></TextField>
        {id && (
          <Button label={"Delete"} handleOnClickAction={handleDelete}></Button>
        )}
      </div>
    </div>
  );
};

export default DeleteImageComponent;
