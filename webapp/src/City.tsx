import {useEffect, useState} from "react";

interface City {
    lat: number,
    lng: number,
    timezone: string
}

interface Times {
    fajr: string
    sun: string
    dhuhr: string
    asr: string
    maghrib: string
    ishaa: string
}

const TimesInitial: Times = {
    fajr: "00:00",
    sun: "00:00",
    dhuhr: "00:00",
    asr: "00:00",
    maghrib: "00:00",
    ishaa: "00:00"
}

interface Date {
    date: string
    hijri: string
    event: string | null
}

const DateInitial: Date = {date: "", hijri: "", event: null}

function City(props: { content: City; path: string[] }) {
    const [date, setDate] = useState<Date>(DateInitial);
    const [times, setTimes] = useState<Times>(TimesInitial);

    useEffect(() => {
        fetch('http://localhost:8080/api/' + props.path.join("/") + "/" + date.date)
            .then(r => r.json()).then(m => setTimes(m));
    }, [date, props.path]);

    useEffect(() => {
        fetch('http://localhost:8080/api/date/' + new Date().toISOString().split("T")[0])
            .then(r => r.json()).then(m => setDate(m));
    }, [new Date().toISOString().split("T")[0]]);

    return (<div className={"flex flex-col w-full h-full"}>
        <p>{date.date}</p>
        <p>{decodeURI(props.path[props.path.length - 1])}</p>
        <p>{date.hijri}</p>
        <p>Imsak: {times.fajr}</p>
        <p>Güneş: {times.sun}</p>
        <p>Öğle: {times.dhuhr}</p>
        <p>İkindi: {times.asr}</p>
        <p>Akşam: {times.maghrib}</p>
        <p>Yatsı: {times.ishaa}</p>
    </div>);


}

export default City;