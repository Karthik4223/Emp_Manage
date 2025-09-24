import React, { createContext, useState, useEffect } from "react";
import {jwtDecode} from "jwt-decode";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem("token"));
  const [username, setUsername] = useState("");
  const [rights, setRights] = useState(() => {
    const stored = localStorage.getItem("rights");
    return stored ? JSON.parse(stored) : [];
  });

  const[rightsNames,setRightsNames] = useState(() => {
    const stored = localStorage.getItem("rightsNames");
    return stored ? JSON.parse(stored) : [];
  });

  useEffect(() => {
  if (token) {
    try {
      const decoded = jwtDecode(token);
      setUsername(decoded.empName);
      if (rights.length === 0) {
        setRights(rights || []);
      }
      if (rightsNames.length === 0) {
        setRightsNames(rightsNames || []);
      }
    } catch (err) {
      setToken(null);
      setUsername("");
      setRights([]);
      setRightsNames([]);
      localStorage.removeItem("token");
      localStorage.removeItem("rights");
      localStorage.removeItem("rightsNames");
    }
  }
}, [token, rights, rightsNames]);

  const login = (token, rightsFromBackend = [], rightsNamesFromBackend = []) => {
    setToken(token);
    localStorage.setItem("token", token);
    setRights(rightsFromBackend);
    localStorage.setItem("rights", JSON.stringify(rightsFromBackend));
    setRightsNames(rightsNamesFromBackend);
    localStorage.setItem("rightsNames", JSON.stringify(rightsNamesFromBackend));
    const decoded = jwtDecode(token);
    setUsername(decoded.empName);
  };

  const logout = () => {
    setToken(null);
    setUsername("");
    setRights([]);
    setRightsNames([]);
    localStorage.removeItem("token");
    localStorage.removeItem("rights");
    localStorage.removeItem("rightsNames");
  };

  const isLoggedIn = !!token;

  return (
    <AuthContext.Provider value={{ isLoggedIn, token, username, rights, rightsNames, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
