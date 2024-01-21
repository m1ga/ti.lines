/**
 * ti.lines
 *
 * Created by Michael Gangolf
 * Copyright (c) 2024 Your Company. All rights reserved.
 */

#import "TiLinesModule.h"
#import "TiBase.h"
#import "TiHost.h"
#import "TiUtils.h"

@implementation TiLinesModule

#pragma mark Internal

// This is generated for your module, please do not change it
- (id)moduleGUID
{
  return @"8c6d8bbd-f2ed-40bb-b8ee-c436d7b47bf0";
}

// This is generated for your module, please do not change it
- (NSString *)moduleId
{
  return @"ti.lines";
}

#pragma mark Lifecycle

- (void)startup
{
  [super startup];
}

MAKE_SYSTEM_PROP(START_CENTER, 0);
MAKE_SYSTEM_PROP(START_BOTTOM, 1);

MAKE_SYSTEM_PROP(TYPE_CURVED, 0);
MAKE_SYSTEM_PROP(TYPE_STRAIGHT, 1);

#pragma Public APIs

@end
