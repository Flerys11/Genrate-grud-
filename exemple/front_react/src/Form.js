import React, { useState, useEffect } from 'react';
import './Form.css';

const Formulaire = () => {
  const [formData, setFormData] = useState({});
  const [columns, setColumns] = useState([]);
  const [ poste, setPoste ] = useState([]);
  const token = localStorage.getItem('token');
  //console.log(token);


const fetchItems = async () => {
	let url = "http://localhost:8080/postes"
	fetch(url, 
		{
			method:'GET',
			headers:{
				'Authorization':`Bearer ${token}`,
				'Content-Type':'application/json'
			}
		})
		.then((response) => response.json())
		.then((data) => {
			if(data.content.length > 0) {
				const objectKeys = Object.keys(data.content[0]);
				setPoste(data.content);
			}
		})
		.catch((error) => {
			console.error('Error:', error);
		});
}


useEffect(() => {
	fetchItems()
}, []);


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

  const handleChange = (e, columnName) => {
    setFormData({
      ...formData,
      [columnName]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    let form = document.getElementById("form");
    let formDatas = createFormData(form);

    const xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/employes";

    xhr.open("POST", url, true);
	if(token){
		xhr.setRequestHeader('Authorization', `Bearer ${token}`)
		xhr.setRequestHeader('Content-Type', 'application/json')
	}

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 201) {
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
		<label htmlFor="nom">nom</label>
		<input type="text" className="form-control" id="nom" name="nom" placeholder="nom" />
	</div>
	<div className="form-group">
		<label htmlFor="prenom">prenom</label>
		<input type="text" className="form-control" id="prenom" name="prenom" placeholder="prenom" />
	</div>
	<div className="form-group">
		<label htmlFor="date_naissance">date_naissance</label>
		<input type="date" className="form-control" id="date_naissance" name="date_naissance" placeholder="date_naissance" />
	</div>
	<div className="form-group">
		<label htmlFor="idposte">poste</label>
		<select className="form-control" id="idposte" name="idposte">
		{poste.map((item) => (
			<option value={item.id}>{item.nom}</option>
		))}
		</select>
	</div>
     <button type="submit">Submit</button>
  </form>
  </>
);
};

export default Formulaire;
