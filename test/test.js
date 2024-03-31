fetch('https://jsonplaceholder.typicode.com/post')
    .then(function(res){ return res.json() })
    .then(function(data){ console.log(data) })