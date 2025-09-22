import React from 'react';
import { useState } from 'react';
import { Routes, Route,useNavigate } from 'react-router-dom';
import './App.css';
import { lazy, Suspense } from "react";

const Home = lazy(() => import("./components/Home"));
const CreateEmployee = lazy(() => import("./components/CreateEmployee"));
const Employee = lazy(() => import("./components/Employee"));
const EmployeeRequest = lazy(() => import("./components/EmployeeRequest"));
const Rights = lazy(() => import("./components/Rights"));
const Header = lazy(() => import("./components/Header"));
const Footer = lazy(() => import("./components/Footer"));
const NavBar = lazy(() => import("./components/NavBar"));
const CreateRight = lazy(() => import("./components/CreateRight"));
const ErrorPage = lazy(() => import("./components/ErrorPage"));
const RightsMapping = lazy(() => import("./components/RightsMapping"));
const SearchEmployee = lazy(() => import("./components/SearchEmployee"));

function App() {
  const [currentPage, setCurrentPage] = useState('home');
  const navigate = useNavigate();

   const handleNavigate = (page, id = null) => {
    if (id) {
      setCurrentPage(`${page}/${id}`);
      navigate(`/${page}/${id}`);
    } else {
      setCurrentPage(page);
      navigate(`/${page}`);
    }
  };

  const renderRoutes = () => {
    return (
    <Suspense fallback={<div>Loading...</div>}>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/create-employee" element={<CreateEmployee />} />
        <Route path="/employee-request" element={<EmployeeRequest />} />
        <Route path="/rights" element={<Rights />} />
        <Route path="/employees" element={<Employee onNavigate={handleNavigate}/>} />
        <Route path="/create-right" element={<CreateRight />} />
        <Route path="/rights-mapping/:empCode" element={<RightsMapping />} />
        <Route path="/search-employee" element={<SearchEmployee />} />
        <Route path="*" element={<ErrorPage />} />
      </Routes>
    </Suspense>
    );
  }

  return (
    <div className="App">
      <div className="app-container">
        <Header />
        <div className="main-layout">
          <NavBar onNavigate={handleNavigate} activePage={currentPage} />
          {renderRoutes()}
        </div>
        <Footer />
      </div>
    </div>
  );
}

export default App;
