import React, { useState, useEffect } from 'react';
import './Login.css';


        const Login = () => {
            const [username, setUsername] = useState('');
            const [password, setPassword] = useState('');
            const [message, setMessage] = useState('');
            const [token, setToken] = useState('');
            const [isLoggedIn, setIsLoggedIn] = useState(false);

            const handleLogin = async () => {
                try {
                    const response = await fetch('http://localhost:8080/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ username, password })
                    });

                    const data = await response.json();

                    if (response.ok) {
                        setToken(data.token);
                        setMessage(data.message);
                        localStorage.setItem('token', data.token);
                        window.location.href = "/liste";
                    } else {
                        setMessage(data.message);
                    }
                } catch (error) {
                    console.error('Erreur lors de la connexion : ', error);
                    setMessage('Une erreur s\'est produite lors de la connexion.');
                }
            };
    


        const handleLogout = () => {

            setToken('');
            setUsername('');
            setPassword('');
            setMessage('');
        };
    




        return (
             <div>
                <h1>Connexion </h1>
                 <div>
                    <label htmlFor="username">Nom d'utilisateur :</label>
                   <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                 </div>
                 <div>
                     <label htmlFor="password">Mot de passe : </label>
                     <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                 <button onClick={handleLogin}>Se connecter </button>
                {message && {message}}
             </div>
        );
    

};

export default Login;
