import React, {useEffect, useState} from 'react';
import List from "./List";
import City from "./City";
import Appbar from "./AppBar";

function App() {

    const path = window.location.search.substring(1).split("/").filter(e => e.length > 0);
    const [model, setModel] = useState<string[] | City>([]);


    useEffect(() => {
        fetch('http://localhost:8080/api/' + path.join("/"))
            .then(r => r.json()).then(m => setModel(m));
    }, [window.location.search]);

    return (
        <div className="App">
            <Appbar path={path}/>
            {
                "lat" in model
                    ? <City content={model as City} path={path}/>
                    : <List content={model as string[]} path={path}/>
            }
        </div>
    );
}

export default App;
