import React, { useContext, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import profile from "../assets/pro.png";
import { FaChevronDown } from "react-icons/fa";
import { BsArrowsFullscreen } from "react-icons/bs";


function Header() {
  const { isLoggedIn, logout, username, empCode } = useContext(AuthContext);
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    setShowDropdown(false);
    logout();
    navigate("/login");
  };

  const handleFullScreenToggle = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen().catch((err) => {
        console.error(`Error attempting to enable full-screen mode: ${err.message} (${err.name})`);
      });
    } else {
      document.exitFullscreen();
    }
  };

  return (
    <header className="header">
      <h3 className="logo">Employee Management System 
         <BsArrowsFullscreen style={{ cursor: 'pointer' , marginLeft: '10px' }} onClick={handleFullScreenToggle} title="Toggle Fullscreen" /></h3>

      {isLoggedIn && (
        <div className="profile-container">

          <div
            className="profile-img"
            onClick={() => setShowDropdown(!showDropdown)}
          >
            <img src={profile} alt="Profile" />
          </div>

          <div className="profile-info" onClick={() => setShowDropdown(!showDropdown)}>
            <span className="username">{username}</span>
            <span className="empcode">{empCode}</span>
          </div>

          <FaChevronDown
            className="dropdown-arrow"
            onClick={() => setShowDropdown(!showDropdown)}
          />

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
