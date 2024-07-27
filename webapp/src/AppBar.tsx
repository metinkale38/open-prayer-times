import {AppBar, Breadcrumbs, Link, Toolbar, Typography} from "@mui/material";


function Appbar(props: { path: string[] }) {

    return <AppBar position="static">
        <Toolbar>
            <Typography  variant="h6" component="div" sx={{flexGrow: 1}}>
                <Link color={"white"} href="#" underline="none">Gebetszeiten</Link>
            </Typography>
            <Breadcrumbs color="white">
                {props.path.map((name, index) =>
                    <Link key={name} color="inherit" href={"?" + props.path.slice(0, index + 1).join("/")}>{decodeURI(name)}</Link>
                )}
            </Breadcrumbs>
        </Toolbar>
    </AppBar>
}

export default Appbar;