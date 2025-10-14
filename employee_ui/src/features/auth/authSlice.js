import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLoggedIn: false,
  empCode: "",
  empName: "",
  rights: [],
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setAuthData: (state, action) => {
      const { empCode, empName, rights } = action.payload;
      state.isLoggedIn = true;
      state.empCode = empCode;
      state.empName = empName;
      state.rights = rights;
    },
    clearAuthData: (state) => {
      state.isLoggedIn = false;
      state.empCode = "";
      state.empName = "";
      state.rights = [];
    },
  },
});

export const { setAuthData, clearAuthData } = authSlice.actions;
export default authSlice.reducer;
