"use strict";

let hour = 0;
let minute = 0;
let second = 0;
let millisecond = 0;

let cron;

document.form_main.start.onclick = () => start();
document.form_main.pause.onclick = () => pause();
document.form_main.reset.onclick = () => reset();

function start() {
  pause();
  cron = setInterval(() => { timer(); }, 10);
}

function pause() {
  clearInterval(cron);
}

function reset() {
  hour = 0;
  minute = 0;
  second = 0;
  millisecond = 0;
  document.getElementById('hour').innerText = '00';
  document.getElementById('minute').innerText = '00';
  document.getElementById('second').innerText = '00';
  document.getElementById('millisecond').innerText = '000';
}

function timer() {
  if ((millisecond += 10) == 1000) {
    millisecond = 0;
    second++;
  }
  if (second == 60) {
    second = 0;
    minute++;
  }
  if (minute == 60) {
    minute = 0;
    hour++;
  }
  document.getElementById('hour').innerText = returnData(hour);
  document.getElementById('minute').innerText = returnData(minute);
  document.getElementById('second').innerText = returnData(second);
  document.getElementById('millisecond').innerText = returnData(millisecond);
}

function returnData(input) {
  return input >= 10 ? input : `0${input}`
}


const quote = ["your biggest commitment must always be to yourself", "take it day by day, don’t stress too much about tomorrow", "if it makes you happy, then it //is not a waste of time", "opportunities don't happen, you create them - chris grosser", "success is walking from failure to failure with no loss of enthusiasm - winston churchill", "great minds discuss ideas; average minds discuss events; small minds discuss people - eleanor roosevelt", "i have not failed. i've just found 10,000 ways that won't work - thomas a. edison", "there are two types of people who will tell you that you cannot make a difference in this world: those who are afraid to try and those who are afraid you will succeed - ray goforth", "i find that the harder i work, the more luck i seem to have - thomas jefferson", "success is the sum of small efforts, repeated day-in and day-out - robert collier", "all progress takes place outside the comfort zone - micheal john bobak", "the function of leadership is to produce more leaders, not more followers - ralph nader", "people rarely succeed unless they have fun in what they are doing - dale carnegie"];

var x =Math.floor(Math.random() * quote.length);
document.getElementById('quotes').innerText = quote[x];
