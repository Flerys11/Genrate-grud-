import React from 'react'

const Login = React.lazy(() => import('./Login'))
const Form = React.lazy(() => import('./Form'))
const Liste = React.lazy(() => import('./Liste'))
const Update = React.lazy(() => import('./Update'))

const routes = [
  { path: '/insert', name: 'Insertion', element: Form },
  { path: '/liste', name: 'Liste', element: Liste },
  { path: '/', name: 'Login', element: Login },
  { path: '/update/:id', name: 'Update', element: Update },
]

export default routes
