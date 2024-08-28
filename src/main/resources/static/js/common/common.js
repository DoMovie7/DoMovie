const navvT = document.querySelectorAll('.nav-top>li');
const navvS =document.querySelectorAll('.navtop-sub');

for(let i =0;i<navvT.length;i++){
	navvT[i].addEventListener('mouseover', function(){
		navvS[i].classList.add('active');
		
		
	});
}

for(let i=0;i<navvT.length;i++){
	navvT[i],addEventListener('mouseout',function(){
		navvS[i].classList.remove('active');
		
		
	});
}