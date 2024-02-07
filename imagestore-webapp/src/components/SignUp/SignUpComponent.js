import { useState } from "react";
import { REGISTER } from "../../redux/actions";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import "./SignUpComponent.css";

const SignUpComponent = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstname, setFirstname] = useState("");
  const [lastname, setLastname] = useState("");
  const [firstnameError, setFirstnameError] = useState("");
  const [lastnameError, setLastnameError] = useState("");
  const [emaiError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const onSignUp = () => {
    setEmailError("");
    setPasswordError("");
    setFirstnameError("");
    setLastnameError("");

    if ("" === firstname) {
      setFirstnameError("Please enter your firstname");
      return;
    }

    if ("" === lastname) {
      setLastnameError("Please enter your lastname");
      return;
    }

    if ("" === email) {
      setEmailError("Please enter your email");
      return;
    }

    if (!/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
      setEmailError("Please enter a valid email");
      return;
    }

    if ("" === password) {
      setPasswordError("Please enter a password");
      return;
    }

    if (password.length < 7) {
      setPasswordError("The password must be 8 characters or longer");
      return;
    }

    dispatch(
      REGISTER({
        firstname,
        lastname,
        email,
        password,
      })
    );
  };

  const redirectToSignIn = () => {
    navigate("/login", { replace: true });
  };

  return (
    <div className={"mainContainer"}>
      <div className={"titleContainer"}>
        <div>Sign Up</div>
      </div>
      <br />
      <div className={"inputContainer"}>
        <input
          value={firstname}
          placeholder="First name"
          onChange={(ev) => setFirstname(ev.target.value)}
          className={"inputBox"}
        />
        <label className="errorLabel">{firstnameError}</label>
      </div>
      <div className={"inputContainer"}>
        <input
          value={lastname}
          placeholder="Last name"
          onChange={(ev) => setLastname(ev.target.value)}
          className={"inputBox"}
        />
        <label className="errorLabel">{lastnameError}</label>
      </div>
      <div className={"inputContainer"}>
        <input
          value={email}
          placeholder="Email"
          onChange={(ev) => setEmail(ev.target.value)}
          className={"inputBox"}
        />
        <label className="errorLabel">{emaiError}</label>
      </div>
      <div className={"inputContainer"}>
        <input
          value={password}
          placeholder="Password"
          onChange={(ev) => setPassword(ev.target.value)}
          className={"inputBox"}
        />
        <label className="errorLabel">{passwordError}</label>
      </div>
      <div className={"inputContainer"}>
        <input
          className={"inputButton"}
          type="button"
          onClick={onSignUp}
          value={"Sign up"}
        />
      </div>
      <div className={"footer-message"}>
        <h3>Already registered yet?</h3>
        <h4 className={"click-here"} onClick={redirectToSignIn}>
          Click here to Sign In
        </h4>
      </div>
    </div>
  );
};

export default SignUpComponent;
