import React, {useState, useEffect} from 'react';
import './App.css';
import { ItemPost } from './components/ItemPost';

function App() {

// react Hook For State Handler
const [data , setData]=useState(null)

// Fetch Function
useEffect(() => {
  fetch("./mock.json").then(
      function(res){
      return res.json()
    }).then(function(data){
    // store Data in State Data Variable
      setData(data)
    }).catch(
      function(err){
        console.log(err, ' error')
      }
    )
}, [])

  return (
    <div className="App">
      <div className='App__newsline'>
        {data?.length && data.map(item => (
          <ItemPost
            key={item.id}
            title={item.title}
            text={item.text}
            imgSrc={item.image}
            link={item.link}
        />
        ))}
      </div>
    </div>
  );
}

export default App;
