import { createContext, useState, useEffect } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useSessionService } from "../services/sessionService";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [empCode, setEmpCode] = useState("");
  const [empName, setEmpName] = useState("");
  const [rightsNames, setRightsNames] = useState([]);
  const { getSessionInfo } = useSessionService();

  const login = ({ empCode, empName, rightsNames }) => {
    setEmpCode(empCode);
    setEmpName(empName);
    setRightsNames(rightsNames);
   
  };

  const logout = async () => {
  try{

    const res = await axios.post("/auth/logout", {}, { withCredentials: true });
    if (res.status === 200) {
      setEmpCode("");
      setEmpName("");
      setRightsNames([]);
      sessionStorage.removeItem("userSession");
      toast.success(res.data);
    }
  }catch(error){
    toast.error(error.message);
  }
};


  useEffect(() => {
  
  const fetchSessionInfo = async () => {
    try {
      const response = await getSessionInfo();

      const { empCode, empName, rightsNames } = response;

      setEmpCode(empCode);
      setEmpName(empName);
      setRightsNames(rightsNames);

      login({ empCode, empName, rightsNames });
    } catch (err) {
      console.error("No active session or failed to fetch:", err);
    }
  };

  fetchSessionInfo();
}, []);


  const isLoggedIn = !!empCode;

  return (
    <AuthContext.Provider
      value={{ isLoggedIn, empCode, empName, rightsNames, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
export default AuthContext;