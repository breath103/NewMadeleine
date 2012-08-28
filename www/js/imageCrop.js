//나중에 이미지 컨테이너의 크기가 변했을때 계속해서 이미지 중앙을 자동으로 잘라내게 하려면,
//우선 이미지 자체는 오리지널 사이즈가 변하지 않기때문에 무시하고 
//이미지 컨테이너는 DOM에다가 ratio만 저장해 놓는다. 
//그리고 컨테이너가 리사이즈 될때마다 ratio만 확인해서 ratio가 바뀌면 그때 전부 다시 계산하고,
//ratio가 그대로라면 	$img.css("top",-($img.height()-$container.outerHeight())/2); 이부분만 실행하게 한다
//물론 ratio 상관없이 계속해서 다시 리사이징을 하게 하는 방법도 있긴 하지만, 이건 퍼포맨스때문에 배제. 
(function($){
	$.fn.cropImageCenter = function() {
		return this.each(function(i, container) {
							var $container = $(container);
							var $img = $container.children("img");
							if (!$img || ($img.length && $img.length > 1)) {
								alert("img not exist");
							} 
							else if ($img[0].complete) // is Loaded
							{
								var containerSize = {
									width : $container.width(),
									height : $container.height()
								};
								var containerRatio = containerSize.height
										/ containerSize.width;
			
								$img.css({
									position : "relative",
									left : "0px",
									top : "0px"
								});
			
								var imgSize = {
									width : 0,
									height : 0
								};
			
								if ($img[0].naturalWidth && $img[0].naturalHeight)
									imgSize = {
										width : $img[0].naturalWidth,
										height : $img[0].naturalHeight
									};
								else {
									$img.css({
										width : "auto",
										height : "auto"
									});
									imgSize = {
										width : $img.width(),
										height : $img.height()
									};
								}
			
								var imgRatio = imgSize.height / imgSize.width;
			
								if (containerRatio < imgRatio) {
									$img.css({
										width : "100%",
										height : "auto"
									});
									$img.css("top", -($img.height() - $container
											.outerHeight()) / 2);
								} else {
									$img.css({
										width : "auto",
										height : "100%"
									});
									$img.css("left", -($img.width() - $container
											.outerWidth()) / 2);
								}
							} 
							else // not loaded
							{
								// when cropImageCenter is called for not loaded Image
								$img.load(function() {
									$(this).parent().cropImageCenter();
								});
							}
						});
};
})(jQuery);