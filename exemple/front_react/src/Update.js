import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './Update.css';

const Formulaire = () => {
  const { id } = useParams();
  const [item, setItem] = useState({});
  const [formData, setFormData] = useState({});
  const [columns, setColumns] = useState([]);
  const [poste, setPoste] = useState([]);
  const token = localStorage.getItem('token');

  const fetchItems = () => {
      let url = "http://localhost:8080/employes/"+id;
      let xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function () {
          if (this.readyState === 4) {
              if (this.status === 200) {
                  let response = JSON.parse(this.response);
                  setItem(response);
                  setFormData(response);
              }
          }
      };
      xhttp.open("GET", url, true);
      if (token) {
          xhttp.setRequestHeader('Authorization', `Bearer ${token}`);
          xhttp.setRequestHeader('Content-Type', 'application/json');
      }
      xhttp.send(null);
  };

  const fetchDataPoste = () => {
      let url = "http://localhost:8080/postes";
      fetch(url, {
          method: 'GET',
          headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
          }
      })
      .then((response) => response.json())
      .then((data) => {
          console.log(data.content);
          if (data.content.length > 0) {
              const objectKeys = Object.keys(data.content[0]);
              setColumns(objectKeys);
              setPoste(data.content);
          }
      })
      .catch((error) => {
          console.error('Error:', error);
      });
  };

  useEffect(() => {
      fetchItems();
      fetchDataPoste();
  }, [id]);

  useEffect(() => {
      const objectKeys = Object.keys(item);
      setColumns(objectKeys);
  }, [item]);

  const handleChange = (e) => {
      const { name, value } = e.target;
      setFormData(prevFormData => ({
          ...prevFormData,
          [name]: value,
      }));
  };

  const createFormData = (form) => {
        let formData = new FormData(form);
        let object = {
		nom: formData.get('nom'),
		prenom: formData.get('prenom'),
		date_naissance: formData.get('date_naissance'),
		poste: {
			id: formData.get('idposte'),
		},

        };
        return object;
      }

  const handleSubmit = (e) => {
    e.preventDefault();

    let form = document.getElementById("form");
    let formDatas = createFormData(form);
    console.log(JSON.stringify(formDatas))

    const xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/employes/" + id;

    xhr.open("PUT", url, true);
    if (token) {
      xhr.setRequestHeader('Authorization', `Bearer ${token}`)
      xhr.setRequestHeader('Content-Type', 'application/json')
    }

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                window.location.href = "/liste";
            } else {
                console.error('Error adding object');
            }
        }
    };

    xhr.send(JSON.stringify(formDatas));
};

   return (
        <>
            <form onSubmit={handleSubmit} id="form">
                <div className="form-group">
                    <label htmlFor="nom">Nom</label>
                    <input type="text" className="form-control" id="nom" name="nom" placeholder="Nom" value={formData.nom || ''} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="prenom">Prénom</label>
                    <input type="text" className="form-control" id="prenom" name="prenom" placeholder="Prénom" value={formData.prenom || ''} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="date_naissance">Date de naissance</label>
                    <input type="date" className="form-control" id="date_naissance" name="date_naissance" placeholder="Date de naissance" value={formData.date_naissance || ''} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="idposte">Poste</label>
                    <select className="form-control" id="idposte" name="idposte" value={formData.idposte || ''} onChange={handleChange} >
                        {poste.map((item) => (
                            <option key={item.id} value={item.id}>{item.nom}</option>
                        ))}
                    </select>
                </div>
                <button type="submit">Submit</button>
            </form>
        </>
    );
};
export default Formulaire;
