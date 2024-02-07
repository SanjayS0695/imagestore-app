import { useNavigate } from "react-router-dom";
import "./HomeComponent.css";

const HomeComponent = () => {
  const navigate = useNavigate();
  const navigateToPage = (path) => {
    navigate(path, { replace: true });
  };

  return (
    <>
      <div className="body-title">
        <h2>Choose what to do from below: </h2>
      </div>
      <div className="action-wrapper">
        <div className="action-set-one">
          <h3 onClick={() => navigateToPage("/upload")}>UPLOAD</h3>
          <h3 onClick={() => navigateToPage("/search")}>SEARCH</h3>
        </div>
        <div className="action-set-two">
          <h3 onClick={() => navigateToPage("/delete")}>DELETE</h3>
          <h3 onClick={() => navigateToPage("/update")}>UPDATE</h3>
        </div>
      </div>
    </>
  );
};

export default HomeComponent;
