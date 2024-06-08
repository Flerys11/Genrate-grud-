import React from 'react';

const WelcomePage = ({ username, handleLogout }) => {
    return (
        <div>
            <h2>Bienvenue, {username} !</h2>
            <p>Vous êtes maintenant connecté.</p>
            <button onClick={handleLogout}>Déconnexion</button>
        </div>
    );
};

export default WelcomePage;