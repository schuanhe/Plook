<template>
    <div class="common-layout-header">
            <img height="50"  src="../assets/favicon.svg"/>
            <p>Plook 一起看！</p>
    </div>
  </template>
  <style>

.common-layout-header {
  padding: 5px;
  display: flex;
  align-items: center;
  justify-content: flex-start; /* aligns items to the left */
}

.common-layout-header p {
  color: rgb(150,80,250);
  font-weight: bold;
  margin: 0; /* removes default margin */
}

.common-layout-header img {
  margin-right: 30px;
  margin-left: 0; /* removes default margin */
}

.title{
    position: relative;
    text-align: center;
    overflow: hidden;
    font-size: 14px;
    color: #999;
}
.title::before,.title::after{
    content: '';
    display: inline-block;
    width: 100%;
    height: 1px;
    position: absolute;
    background: #ccc;
    top: 50%;
}
.title::before{
    margin-left: -10px;
    transform: translateX(-100%);
}
.title::after{
    margin-left: 10px;
}


</style>