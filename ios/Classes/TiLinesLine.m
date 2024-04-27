//
//  TiLinesViewProxy.m
//  ti.lines
//
//  Created by miga on 21.01.24.
//

#import "TiLinesLine.h"
#import "TiUtils.h"
#import "UIKit/UIBezierPath.h"
#import "UIBezierPath+Smoothing.h"

@implementation TiLinesLine

- (void) initializeState {
    square = [[UIView alloc] initWithFrame:[self frame]];
    [self addSubview:square];
    [super initializeState];
    circleLayer = [CAShapeLayer layer];
}

- (void) frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds {
    [TiUtils setView:square positionRect:bounds];
}

- (void)setColor_:(id) color{
    square.backgroundColor = [[TiUtils colorValue:color]_color];
}
- (void)setBackgroundColor_:(id) color{
    bgColor = [[TiUtils colorValue:color]_color];
}

- (void)setValues_:(id) value{
    ENSURE_ARRAY(value);
    values = value;
}

- (void)redraw:(id)args
{
    [self setNeedsDisplay];
}

- (void)clear:(id)args
{
    square.layer.sublayers = nil;
}

- (void)setLineWidth_:(id) value{
    lineWidth = [TiUtils intValue:value];
}
- (void)setMaxValue_:(id) value{
    maxValue = [TiUtils intValue:value];
}
- (void)setYLines_:(id) value{
    yLines = [TiUtils intValue:value];
}
- (void)setDotSize_:(id) value{
    dotSize = [TiUtils intValue:value];
}
- (void)setLineColor_:(id) value{
    lineColor = [TiUtils colorValue:value];
}
- (void)setDotColor_:(id) value{
    dotColor = [TiUtils colorValue:value];
}
- (void)setYLineColor_:(id) value{
    yLineColor = [TiUtils colorValue:value];
}
- (void)setLineType_:(id) value{
    lineType = [TiUtils intValue:value];
}
- (void)setStartAt_:(id) value{
    startAt = [TiUtils intValue:value];
}
- (void)setShowDots_:(id) value{
    showDots = [TiUtils boolValue:value];
}
-(void)drawRect:(CGRect)rect {
    square.layer.sublayers = nil;

    //draw a line
    if (yLines > 0) {
        CAShapeLayer *shapeLayer2 = [CAShapeLayer layer];
        [shapeLayer2 setStrokeColor:[yLineColor _color].CGColor];
        [shapeLayer2 setLineWidth:1];
        [shapeLayer2 setLineJoin:kCALineJoinRound];
        [shapeLayer2 setLineDashPattern:@[@(2), @(2)]];
        
        CGMutablePathRef path2 = CGPathCreateMutable();
        CGFloat stepY = square.bounds.size.height / yLines;
        for (NSInteger i = 0; i<yLines;i++) {
            CGPathMoveToPoint(path2, NULL, 0,square.bounds.size.height - i*stepY);
            CGPathAddLineToPoint(path2, NULL, square.bounds.size.width, square.bounds.size.height -i*stepY);
        }
        [shapeLayer2 setPath:path2];
        CGPathRelease(path2);
        
        [square.layer addSublayer:shapeLayer2];
    }
    
    UIBezierPath *bezierPath = [UIBezierPath bezierPath];
    CGFloat stepX = square.bounds.size.width / (values.count - 1);
    CGFloat startX = 0;
    CGFloat startPosition = square.bounds.size.height * 0.5;
    if (startAt == 1) {
        // start at bottom
        startPosition = square.bounds.size.height;
    } else {
        // start in center
        maxValue /= 0.5;
    }

    // starting point
    CGFloat hPoint = [TiUtils intValue: values[0]];
    hPoint = (square.bounds.size.height / maxValue) * hPoint;
    [bezierPath moveToPoint:(CGPointMake(0, -hPoint + startPosition)) ];

    [circleLayer setFillColor:[dotColor _color].CGColor];
    UIBezierPath *circlePath = [UIBezierPath bezierPath];
    
    // draw line
    for (NSInteger i = 0; i < values.count; i++) {
        CGFloat hPoint = [TiUtils intValue: values[i]];
        hPoint = (square.bounds.size.height / maxValue) * hPoint;
        [bezierPath addLineToPoint: CGPointMake(startX, -hPoint + startPosition)];
        
        // dots
        if (showDots) {
            CGRect box = CGRectMake(startX - (dotSize*0.5),  -hPoint + startPosition - (dotSize * 0.5), dotSize, dotSize);
            UIBezierPath *ballBezierPath = [UIBezierPath bezierPathWithOvalInRect:box];
            [circlePath appendPath:ballBezierPath];
        }
        // /dots
        
        startX += stepX;
    }

    if (showDots) {
        [circleLayer setPath:circlePath.CGPath];
    }
    CAShapeLayer *shapeLayer = [CAShapeLayer layer];
    if (lineType == 0) {
        // smooth path
        UIBezierPath *smoothPath = [bezierPath smoothedPathWithGranularity: 10 minY:0 maxY:100];
        shapeLayer.path = [smoothPath CGPath];
    } else {
        // straight path
        shapeLayer.path = [bezierPath CGPath];
    }
    shapeLayer.strokeColor = [lineColor _color].CGColor;
    shapeLayer.lineWidth = lineWidth;
    shapeLayer.fillColor = [[UIColor clearColor] CGColor];

    [square.layer addSublayer:shapeLayer];
    if (showDots){
        [square.layer addSublayer:circleLayer];
    }
    
    UIBezierPath *path = [UIBezierPath bezierPath];

    
}

@end
