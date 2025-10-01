import React, { useState, useContext, lazy, Suspense } from 'react';
import { Routes, Route, useNavigate,Navigate } from 'react-router-dom';
import { AuthContext } from './context/AuthContext';

const Home = lazy(() => import("./pages/Home"));
const ErrorPage = lazy(() => import("./pages/ErrorPage"));
const LoginPage = lazy(() => import("./pages/LoginPage"));
const Header = lazy(() => import("./components/Header"));
const Footer = lazy(() => import("./components/Footer"));
const NavBar = lazy(() => import("./components/NavBar"));
const CreateEmployee = lazy(() => import("./features/employees/CreateEmployee"));
const Employee = lazy(() => import("./features/employees/Employee"));
const EmployeeRequest = lazy(() => import("./features/employees/EmployeeRequest"));
const SearchEmployee = lazy(() => import("./features/employees/SearchEmployee"));
const Rights = lazy(() => import("./features/rights/Rights"));
const CreateRight = lazy(() => import("./features/rights/CreateRight"));
const RightsMapping = lazy(() => import("./features/rights/RightsMapping"));
const CreateEmployeeExcel = lazy(() => import("./features/employees/CreateEmployeeExcel"));

const PrivateRoute = ({ children }) => {
  const { isLoggedIn } = useContext(AuthContext);
  return isLoggedIn ? children : <LoginPage />;
};

function App() {
  const [currentPage, setCurrentPage] = useState('home');
  const navigate = useNavigate();
  const { isLoggedIn } = useContext(AuthContext);

  const handleNavigate = (page, id = null, data = null) => {
    if(data){
      navigate(`/${page}`, { state: { prefillData: data } });
      setCurrentPage(`${page}`);
      return;
    }
    if (id) {
      setCurrentPage(`${page}/${id}`);
      navigate(`/${page}/${id}`);
    } else {
      setCurrentPage(page);
      navigate(`/${page}`);
    }
  };

  const renderRoutes = () => (
    <Suspense fallback={<div>Loading...</div>}>
      <Routes>

        <Route path="/login"  element={isLoggedIn ? <Navigate to="/home" /> : <LoginPage />} />

        <Route path="/" element={
          <PrivateRoute>
            <Home />
          </PrivateRoute>
        } />
        <Route path="/home" element={
          <PrivateRoute>
            <Home />
          </PrivateRoute>
        } />
        <Route path="/create-employee" element={
          <PrivateRoute>
            <CreateEmployee />
          </PrivateRoute>
        } />
        <Route path="/employee-request" element={
          <PrivateRoute>
            <EmployeeRequest />
          </PrivateRoute>
        } />
        <Route path="/rights" element={
          <PrivateRoute>
            <Rights />
          </PrivateRoute>
        } />
        <Route path="/employees" element={
          <PrivateRoute>
            <Employee onNavigate={handleNavigate}/>
          </PrivateRoute>
        } />

        <Route path="/create-right" element={
          <PrivateRoute>
            <CreateRight />
          </PrivateRoute>
        } />

        <Route path="/rights-mapping/:empCode" element={
          <PrivateRoute>
            <RightsMapping />
          </PrivateRoute>
        } />
        <Route path="/search-employee" element={
          <PrivateRoute>
            <SearchEmployee />
          </PrivateRoute>
        } />

        <Route path="/create-employee-excel" element={
          <PrivateRoute>
            <CreateEmployeeExcel />
          </PrivateRoute>
        } />

        <Route path="*" element={<ErrorPage />} />
      </Routes>
    </Suspense>
  );

  return (
    <div className="App">
      <div className="app-container">
        <Header />
        <div className="main-layout">
          {isLoggedIn && <NavBar onNavigate={handleNavigate} activePage={currentPage} />}
          {renderRoutes()}
        </div>
        <Footer />
      </div>
    </div>
  );
}

export default App;
