import {ListItem, ListItemButton} from "@mui/material";

function List(props: { content: string[]; path: string[] }) {
    return (<div>
        {props.content.map(e =>
            <ListItemButton key={e}
                            href={`?${[...props.path, e].join("/")}`}><ListItem>{decodeURI(e)}</ListItem></ListItemButton>
        )}
    </div>);


}

export default List;