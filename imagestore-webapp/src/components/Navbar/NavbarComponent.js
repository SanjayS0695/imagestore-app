import { useNavigate } from "react-router-dom";
import "./NavbarComponent.css";

const NavbarComponent = () => {
  const navigate = useNavigate();

  const navigateToPage = (path) => {
    navigate(path, { replace: true });
  };

  return (
    <div className="navbar-wrapper">
      <div className="header-one" onClick={() => navigateToPage("/")}>
        <h3>Home</h3>
      </div>
      <div className="header-two" onClick={() => navigateToPage("/view")}>
        <h3>See all images</h3>
      </div>
    </div>
  );
};

export default NavbarComponent;
