import React, { Component, Suspense } from 'react'
import { BrowserRouter, Outlet, Route, Routes } from 'react-router-dom'
import routes from './routes'

const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)



class App extends Component {

  
  render() {
    return (
      <Suspense fallback={loading}>
        <Routes>
          {routes.map((route, index) => (
            <Route
              key={index}
              path={route.path}
              element={<route.element />}
            />
          ))}
          
        </Routes>
         </Suspense>

    )
  }
}

export default App

