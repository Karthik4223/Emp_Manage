import React, { useContext, useState} from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

function Header() {
  const { isLoggedIn, logout, username } = useContext(AuthContext);
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    setShowDropdown(false);
    logout();
    navigate("/login");
  };

  return (
    <header className="header">
      <h3>Employee Management System</h3>
      {isLoggedIn && (
        <div className="profile-container">
          <div
            className="profile-name"
            onClick={() => setShowDropdown(!showDropdown)}
          >
            {username}
          </div>
          {showDropdown && (
            <div className="dropdown">
              <button onClick={handleLogout}>Logout</button>
            </div>
          )}
        </div>
      )}
    </header>
  );
}

export default Header;
