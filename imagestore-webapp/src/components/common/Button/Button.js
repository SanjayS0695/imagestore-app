import "./Button.css";

const Button = ({ label, handleOnClickAction }) => {
  return (
    <>
      <div className="button" onClick={handleOnClickAction}>
        <h3>{label}</h3>
      </div>
    </>
  );
};

export default Button;
