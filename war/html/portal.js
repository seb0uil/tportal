
$( document ).ready(function() {
	$('.portlet-menu-bar').hover(
			function(){
				$(this).children().fadeIn(400);
				//$(this).children().show();
			}
			,function(){
				$(this).children().fadeOut(400);
				//$(this).children().hide();
				$(this).children('ul').children('li').attr('class','dropdown');
			}
	);


	/* gestion de la checkbox pour l'affichage des controls des portlets */
	$('#checkbox-control').change(function() {
		if ($('#checkbox-control').is(':checked')) {
			$('.portlet-menu-bar').show();
			$('.portlet-menu').show();
		} else {
			$('.portlet-menu-bar').hide();
			$('.portlet-menu').hide();
		}
	});

});

(function( $ ) {
	$.fn.sbealert = function() {
		alert("coucou");
	};
}( jQuery ));

$().sbealert();


